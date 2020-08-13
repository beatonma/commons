package org.beatonma.commons.app.memberprofile

import android.animation.ValueAnimator
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import kotlinx.coroutines.Job
import org.beatonma.commons.R
import org.beatonma.commons.app.memberprofile.adapter.CurrentAdapter
import org.beatonma.commons.app.memberprofile.adapter.FinancialInterestsAdapter
import org.beatonma.commons.app.memberprofile.adapter.PhysicalAddressAdapter
import org.beatonma.commons.app.ui.Interpolation
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.Themed
import org.beatonma.commons.app.ui.data.WeblinkData
import org.beatonma.commons.app.ui.recyclerview.adapter.*
import org.beatonma.commons.app.ui.recyclerview.defaultSpace
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.app.ui.recyclerview.viewholder.staticViewHolderOf
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.databinding.HistoryviewBinding
import org.beatonma.commons.databinding.RecyclerviewBinding
import org.beatonma.commons.databinding.ViewCollapsingGroupBinding
import org.beatonma.commons.databinding.ViewScrollableChipsBinding
import org.beatonma.commons.kotlin.extensions.hide
import org.beatonma.commons.kotlin.extensions.inflate
import org.beatonma.commons.kotlin.extensions.show
import org.beatonma.commons.kotlin.extensions.stringCompat

private const val TAG = "ProfileDataAdapter"

/**
 * DISPLAYED
 * - weblinks
 * - current position/status
 * - history
 * - contact/physical addresses
 * - financial interests
 *
 * TODO
 * - experiences
 * - topics of interest
 * - posts
 */


internal enum class LayoutType {
    HORIZONTAL {
        override fun setup(recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter, showSeparators: Boolean) {
            recyclerView.setup(
                adapter,
                orientation = RecyclerView.HORIZONTAL,
                space = defaultSpace(recyclerView.context, orientation = RecyclerView.HORIZONTAL, overscroll = true),
                showSeparators = showSeparators
            )
        }
    },
    VERTICAL {
        override fun setup(recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter, showSeparators: Boolean) {
            recyclerView.setup(adapter, showSeparators = showSeparators)
        }
    },
    ;

    abstract fun setup(recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter, showSeparators: Boolean = false)
}


/**
 * Top level adapter - many child RecyclerViews for [ProfileData] types.
 */

class ProfileDataAdapter(
    private var asyncDiffHost: AsyncDiffHost? = null
): LoadingAdapter<ProfileData<Any>>(), Themed {

    override var theme: PartyColors? = null

    override fun getItemViewType(position: Int): Int {
        val data: ProfileData<*>? = items?.get(position)
        return data?.type?.ordinal ?: super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ProfileData.ProfileDataType.values()
            .firstOrNull { it.ordinal == viewType }
            ?.getViewHolder(parent)
            ?: super.onCreateViewHolder(parent, viewType)

    override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return staticViewHolderOf(parent.inflate(R.layout.invisible))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = items?.get(position) ?: return
        when (holder) {
            is AbstractProfileDataHolder<*> -> holder.bind(data, theme, asyncDiffHost)
            is GenericTypedViewHolder<*> -> {
                @Suppress("UNCHECKED_CAST")
                (holder as GenericTypedViewHolder<ProfileData<*>>).bind(data)
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }
}


private fun createHistoryHolder(parent: ViewGroup): GenericTypedViewHolder<ProfileData<Temporal>> =
    object: GenericTypedViewHolder<ProfileData<Temporal>>(parent.inflate(R.layout.historyview)) {
        val vh = HistoryviewBinding.bind(itemView)
        override fun bind(item: ProfileData<Temporal>) {
            vh.historyview.setHistory(item.items)
        }
    }


private fun createCurrentHolder(parent: ViewGroup): AbstractProfileDataHolder<Any> =
    object: CollapsibleProfileDataHolder<Any>(
        parent,
        CurrentAdapter(),
        title = parent.context.stringCompat(R.string.member_current_title),
    ) {}


private fun createWeblinksHolder(parent: ViewGroup): GenericTypedViewHolder<ProfileData<WeblinkData>> =
    object: GenericTypedViewHolder<ProfileData<WeblinkData>>(parent.inflate(R.layout.view_scrollable_chips)) {
        val vh = ViewScrollableChipsBinding.bind(itemView)
        override fun bind(item: ProfileData<WeblinkData>) {
            vh.chipsView.setData(item.items)
        }
    }


private fun createAddressHolder(parent: ViewGroup): AbstractProfileDataHolder<PhysicalAddress> =
    object: CollapsibleProfileDataHolder<PhysicalAddress>(
        parent,
        PhysicalAddressAdapter(),
        title = parent.context.stringCompat(R.string.member_write_to),
        showSeparators = true
    ) {}


private fun createFinancialInterestsHolder(parent: ViewGroup): AbstractProfileDataHolder<FinancialInterest> =
    object: CollapsibleProfileDataHolder<FinancialInterest>(
        parent,
        FinancialInterestsAdapter(),
        title = parent.context.stringCompat(R.string.member_financial_interests),
        showSeparators = true
    ) {}


internal abstract class AbstractProfileDataHolder<D>(
    parent: ViewGroup,
    @LayoutRes layoutId: Int,
    val adapter: TypedAdapter<D>
): RecyclerView.ViewHolder(parent.inflate(layoutId)), AsyncDiff {
    override var diffJob: Job? = null  // Each child adapter should have its own diffJob to avoid collisions.

    @Suppress("UNCHECKED_CAST")
    open fun bind(data: ProfileData<Any>, theme: PartyColors?, asyncDiffHost: AsyncDiffHost?) {
        if (adapter is Themed) adapter.theme = theme

        when (asyncDiffHost) {
            null -> adapter.diffSync(data.items as List<D>)
            else -> diffAdapterItems(asyncDiffHost, adapter, data.items as List<D>)
        }
    }
}


internal abstract class ProfileDataHolder<D>(
    parent: ViewGroup,
    adapter: ThemedAdapter<D>,
    layoutType: LayoutType = LayoutType.VERTICAL,
    showSeparators: Boolean = false,
): AbstractProfileDataHolder<D>(parent, R.layout.recyclerview, adapter) {
    private val vh = RecyclerviewBinding.bind(itemView)
    init {
        layoutType.setup(vh.recyclerview, adapter, showSeparators)
    }
}

internal abstract class CollapsibleProfileDataHolder<D>(
    parent: ViewGroup,
    adapter: ThemedCollapsibleAdapter<D>,
    layoutType: LayoutType = LayoutType.VERTICAL,
    title: String,
    initialCollapsed: Boolean = true,
    collapsedVisibleItems: Int = 3,
    showSeparators: Boolean = false,
): AbstractProfileDataHolder<D>(parent, R.layout.view_collapsing_group, adapter) {
    private val vh = ViewCollapsingGroupBinding.bind(itemView)
    init {
        layoutType.setup(vh.recyclerview, adapter, showSeparators)
        adapter.apply {
            collapsedItemsVisible = collapsedVisibleItems
            setCollapsed(initialCollapsed)
        }
        vh.title.text = title
    }

    override fun bind(data: ProfileData<Any>, theme: PartyColors?, asyncDiffHost: AsyncDiffHost?) {
        super.bind(data, theme, asyncDiffHost)

        when((adapter as CollapsibleAdapter).isCollapsible) {
            true -> {
                vh.header.setOnClickListener { toggle() }
                vh.toggle.show()
            }
            false -> {
                vh.header.setOnClickListener(null)
                vh.toggle.hide()
            }
        }
    }

    private fun toggle() {
        TransitionManager.beginDelayedTransition(vh.root)
        animateDropdownIcon(!(adapter as CollapsibleAdapter).toggle())
    }

    private fun animateDropdownIcon(collapsed: Boolean) {
        val values = when (collapsed) {
            true -> floatArrayOf(0F, 180F)
            false -> floatArrayOf(180F, 0F)
        }
        ValueAnimator.ofFloat(*values).apply {
            addUpdateListener { animator ->
                vh.toggle.rotation = animator.animatedValue as Float
            }
            interpolator = Interpolation.motion
            duration = 300
        }.start()
    }
}


sealed class ProfileData<T>(internal val type: ProfileDataType) {
    abstract val items: List<T>

    data class Current(override val items: List<Any>): ProfileData<Any>(ProfileDataType.CURRENT)
    data class History(override val items: List<Temporal>): ProfileData<Temporal>(ProfileDataType.HISTORY)
    data class Weblinks(override val items: List<WeblinkData>): ProfileData<WeblinkData>(ProfileDataType.WEBLINKS)
    data class Addresses(override val items: List<PhysicalAddress>): ProfileData<PhysicalAddress>(ProfileDataType.ADDRESSES)
    data class FinancialInterests(override val items: List<FinancialInterest>): ProfileData<FinancialInterest>(ProfileDataType.FINANCIAL)

    fun isEmpty() = items.isEmpty()

    internal enum class ProfileDataType {
        WEBLINKS {
            override fun getViewHolder(parent: ViewGroup) = createWeblinksHolder(parent)
        },
        CURRENT {
            override fun getViewHolder(parent: ViewGroup) = createCurrentHolder(parent)
        },
        HISTORY {
            override fun getViewHolder(parent: ViewGroup)= createHistoryHolder(parent)
        },
        ADDRESSES {
            override fun getViewHolder(parent: ViewGroup) = createAddressHolder(parent)
        },
        FINANCIAL {
            override fun getViewHolder(parent: ViewGroup) = createFinancialInterestsHolder(parent)
        }
        ;
        abstract fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    }
}
