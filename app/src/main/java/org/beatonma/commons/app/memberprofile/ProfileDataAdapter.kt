package org.beatonma.commons.app.memberprofile

import android.animation.ValueAnimator
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import org.beatonma.commons.R
import org.beatonma.commons.app.memberprofile.adapter.*
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.Themed
import org.beatonma.commons.app.ui.data.WeblinkData
import org.beatonma.commons.app.ui.recyclerview.*
import org.beatonma.commons.app.ui.recyclerview.viewholder.StaticViewHolder
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.databinding.RecyclerviewBinding
import org.beatonma.commons.databinding.ViewCollapsingGroupBinding
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.lib.graphic.core.utility.AnimationUtils

private const val TAG = "ProfileDataAdapter"


internal enum class LayoutType {
    HORIZONTAL {
        override fun setup(recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter) {
            recyclerView.setup(adapter, layoutManager = horizontalLinearLayoutManager(recyclerView.context), space = RvSpacing(
                horizontalItemSpace = recyclerView.context.dimenCompat(R.dimen.flow_gap_horizontal)
            ))
        }
    },
    VERTICAL {
        override fun setup(recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter) {
            recyclerView.setup(adapter)
        }
    },
    STAGGERED_GRID {
        override fun setup(recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter) {
            recyclerView.setupGrid(adapter, columnCount = 1)
        }
    };

    abstract fun setup(recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter)
}


/**
 * Top level adapter - many child RecyclerViews for [ProfileData] types.
 */

class ProfileDataAdapter: LoadingAdapter<ProfileData<Any>>(), Themed {

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
        return object: StaticViewHolder(parent.inflate(R.layout.invisible)) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = items?.get(position) ?: return
        when (holder) {
            is AbstractProfileDataHolder<*> -> holder.bind(data, theme)
            else -> super.onBindViewHolder(holder, position)
        }
    }
}

private fun createHistoryHolder(parent: ViewGroup): AbstractProfileDataHolder<HistoryItem<*>> =
    object: CollapsibleProfileDataHolder<HistoryItem<*>>(
        parent,
        HistoryAdapter(),
        title = parent.context.stringCompat(R.string.member_history_title),
    ) {}

private fun createCurrentHolder(parent: ViewGroup): AbstractProfileDataHolder<Any> =
    object: CollapsibleProfileDataHolder<Any>(
        parent,
        CurrentAdapter(),
        title = parent.context.stringCompat(R.string.member_current_title),
    ) {}

private fun createWeblinksHolder(parent: ViewGroup): AbstractProfileDataHolder<WeblinkData> =
    object: ProfileDataHolder<WeblinkData>(parent,
        WeblinkAdapter(),
        LayoutType.HORIZONTAL,
    ) {}

private fun createAddressHolder(parent: ViewGroup): AbstractProfileDataHolder<PhysicalAddress> =
    object: CollapsibleProfileDataHolder<PhysicalAddress>(
        parent,
        PhysicalAddressAdapter(),
        title = parent.context.stringCompat(R.string.member_write_to),
    ) {}

private fun createFinancialInterestsHolder(parent: ViewGroup): AbstractProfileDataHolder<FinancialInterest> =
    object: CollapsibleProfileDataHolder<FinancialInterest>(
        parent,
        FinancialInterestsAdapter(),
        title = parent.context.stringCompat(R.string.member_financial_interests)
    ) {}


internal abstract class AbstractProfileDataHolder<D>(
    parent: ViewGroup,
    @LayoutRes layoutId: Int,
    val adapter: TypedAdapter<D>
): RecyclerView.ViewHolder(parent.inflate(layoutId)) {

    @Suppress("UNCHECKED_CAST")
    open fun bind(data: ProfileData<Any>, theme: PartyColors?) {
        if (adapter is Themed) adapter.theme = theme
        adapter.items = data.items as List<D>
    }
}


internal abstract class ProfileDataHolder<D>(
    parent: ViewGroup,
    adapter: ThemedAdapter<D>,
    layoutType: LayoutType = LayoutType.VERTICAL
): AbstractProfileDataHolder<D>(parent, R.layout.recyclerview, adapter) {
    private val vh = RecyclerviewBinding.bind(itemView)
    init {
        layoutType.setup(vh.recyclerview, adapter)
    }
}

internal abstract class CollapsibleProfileDataHolder<D>(
    parent: ViewGroup,
    adapter: ThemedCollapsibleAdapter<D>,
    layoutType: LayoutType = LayoutType.VERTICAL,
    title: String,
    initialCollapsed: Boolean = true,
    collapsedVisibleItems: Int = 3
): AbstractProfileDataHolder<D>(parent, R.layout.view_collapsing_group, adapter) {
    private val vh = ViewCollapsingGroupBinding.bind(itemView)
    init {
        layoutType.setup(vh.recyclerview, adapter)
        adapter.apply {
            collapsedItemsVisible = collapsedVisibleItems
            setCollapsed(initialCollapsed)
        }
        vh.title.text = title
    }

    override fun bind(data: ProfileData<Any>, theme: PartyColors?) {
        super.bind(data, theme)
        when((adapter as CollapsibleAdapter).isCollapsible()) {
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
            interpolator = AnimationUtils.getMotionInterpolator()
            duration = 300
        }.start()
    }
}


sealed class ProfileData<T>(internal val type: ProfileDataType) {
    abstract val items: List<T>

    data class Current(override val items: List<Any>): ProfileData<Any>(ProfileDataType.CURRENT)
    data class History(override val items: List<HistoryItem<*>>): ProfileData<HistoryItem<*>>(ProfileDataType.HISTORY)
    data class Weblinks(override val items: List<WeblinkData>): ProfileData<WeblinkData>(ProfileDataType.WEBLINKS)
    data class Addresses(override val items: List<PhysicalAddress>): ProfileData<PhysicalAddress>(ProfileDataType.ADDRESSES)
    data class FinancialInterests(override val items: List<FinancialInterest>): ProfileData<FinancialInterest>(ProfileDataType.FINANCIAL)

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
        abstract fun getViewHolder(parent: ViewGroup): AbstractProfileDataHolder<*>
    }
}
