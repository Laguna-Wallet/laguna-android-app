package io.novafoundation.nova.feature_staking_impl.presentation.validators.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.chrisbanes.insetter.applyInsetter
import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.presentation.LoadingState
import io.novafoundation.nova.common.utils.makeGone
import io.novafoundation.nova.common.utils.makeVisible
import io.novafoundation.nova.common.utils.setVisible
import io.novafoundation.nova.feature_staking_api.di.StakingFeatureApi
import io.novafoundation.nova.feature_staking_impl.R
import io.novafoundation.nova.feature_staking_impl.di.StakingFeatureComponent
import io.novafoundation.nova.feature_staking_impl.presentation.validators.current.model.NominatedValidatorModel
import kotlinx.android.synthetic.main.fragment_current_validators.currentValidatorsContainer
import kotlinx.android.synthetic.main.fragment_current_validators.currentValidatorsList
import kotlinx.android.synthetic.main.fragment_current_validators.currentValidatorsOversubscribedMessage
import kotlinx.android.synthetic.main.fragment_current_validators.currentValidatorsProgress
import kotlinx.android.synthetic.main.fragment_current_validators.currentValidatorsToolbar

class CurrentValidatorsFragment : BaseFragmentOld<CurrentValidatorsViewModel>(), CurrentValidatorsAdapter.Handler {

    lateinit var adapter: CurrentValidatorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_current_validators, container, false)
    }

    override fun initViews() {
        currentValidatorsContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }

        adapter = CurrentValidatorsAdapter(this)
        currentValidatorsList.adapter = adapter

        currentValidatorsList.setHasFixedSize(true)

        currentValidatorsToolbar.setHomeButtonListener { viewModel.backClicked() }

        currentValidatorsToolbar.setRightActionClickListener { viewModel.changeClicked() }
    }

    override fun inject() {
        FeatureUtils.getFeature<StakingFeatureComponent>(
            requireContext(),
            StakingFeatureApi::class.java
        )
            .currentValidatorsFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: CurrentValidatorsViewModel) {
        viewModel.currentValidatorModelsLiveData.observe { loadingState ->
            when (loadingState) {
                is LoadingState.Loading -> {
                    currentValidatorsList.makeGone()
                    currentValidatorsProgress.makeVisible()
                }

                is LoadingState.Loaded -> {
                    currentValidatorsList.makeVisible()
                    currentValidatorsProgress.makeGone()

                    adapter.submitList(loadingState.data)
                }
            }
        }

        viewModel.shouldShowOversubscribedNoRewardWarning.observe {
            currentValidatorsOversubscribedMessage.setVisible(it)
        }
    }

    override fun infoClicked(validatorModel: NominatedValidatorModel) {
        viewModel.validatorInfoClicked(validatorModel.addressModel.address)
    }
}
