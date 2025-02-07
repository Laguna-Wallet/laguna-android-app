package io.novafoundation.nova.feature_crowdloan_impl.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import dev.chrisbanes.insetter.applyInsetter
import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.mixin.impl.setupCustomDialogDisplayer
import io.novafoundation.nova.common.presentation.LoadingState
import io.novafoundation.nova.common.utils.setVisible
import io.novafoundation.nova.common.view.shape.addRipple
import io.novafoundation.nova.common.view.shape.getBlurDrawable
import io.novafoundation.nova.feature_crowdloan_api.data.network.blockhain.binding.ParaId
import io.novafoundation.nova.feature_crowdloan_api.di.CrowdloanFeatureApi
import io.novafoundation.nova.feature_crowdloan_impl.R
import io.novafoundation.nova.feature_crowdloan_impl.di.CrowdloanFeatureComponent
import io.novafoundation.nova.feature_wallet_api.presentation.mixin.assetSelector.setupAssetSelector
import kotlinx.android.synthetic.main.fragment_crowdloans.crowdloanAbout
import kotlinx.android.synthetic.main.fragment_crowdloans.crowdloanAssetSelector
import kotlinx.android.synthetic.main.fragment_crowdloans.crowdloanContainer
import kotlinx.android.synthetic.main.fragment_crowdloans.crowdloanList
import kotlinx.android.synthetic.main.fragment_crowdloans.crowdloanMainDescription
import kotlinx.android.synthetic.main.fragment_crowdloans.crowdloanMyContributions
import kotlinx.android.synthetic.main.fragment_crowdloans.crowdloanPlaceholder
import kotlinx.android.synthetic.main.fragment_crowdloans.crowdloanProgress
import javax.inject.Inject

class CrowdloanFragment : BaseFragmentOld<CrowdloanViewModel>(), CrowdloanAdapter.Handler {

    @Inject protected lateinit var imageLoader: ImageLoader

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        CrowdloanAdapter(imageLoader, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_crowdloans, container, false)
    }

    override fun initViews() {
        crowdloanContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }

        crowdloanList.adapter = adapter

        with(requireContext()) {
            crowdloanAbout.background = getBlurDrawable()
            crowdloanMyContributions.background = addRipple(getBlurDrawable())
        }

        crowdloanMyContributions.setOnClickListener { viewModel.myContributionsClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<CrowdloanFeatureComponent>(
            requireContext(),
            CrowdloanFeatureApi::class.java
        )
            .crowdloansFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: CrowdloanViewModel) {
        setupAssetSelector(crowdloanAssetSelector, viewModel, imageLoader)
        setupCustomDialogDisplayer(viewModel)

        viewModel.crowdloanModelsFlow.observe { loadingState ->
            // GONE state does not trigger re-render on data change (i.e. when we want to drop outdated list)
            crowdloanList.setVisible(loadingState is LoadingState.Loaded && loadingState.data.isNotEmpty(), falseState = View.INVISIBLE)
            crowdloanPlaceholder.setVisible(loadingState is LoadingState.Loaded && loadingState.data.isEmpty())
            crowdloanProgress.setVisible(loadingState is LoadingState.Loading)

            if (loadingState is LoadingState.Loaded) {
                adapter.submitList(loadingState.data)
            } else {
                // to prevent outdated information appear for a moment between next chunk submitted and rendered
                adapter.submitList(emptyList())
            }
        }

        viewModel.myContributionsCount.observe {
            crowdloanMyContributions.setInProgress(it is LoadingState.Loading)
            crowdloanMyContributions.setBadgeText((it as? LoadingState.Loaded)?.data)
        }

        viewModel.mainDescription.observe(crowdloanMainDescription::setText)
    }

    override fun crowdloanClicked(paraId: ParaId) {
        viewModel.crowdloanClicked(paraId)
    }
}
