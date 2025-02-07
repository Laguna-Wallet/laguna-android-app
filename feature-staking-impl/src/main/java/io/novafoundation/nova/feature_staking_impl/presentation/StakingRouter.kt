package io.novafoundation.nova.feature_staking_impl.presentation

import androidx.lifecycle.Lifecycle
import com.github.terrakok.cicerone.ResultListener
import io.novafoundation.nova.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload
import io.novafoundation.nova.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable
import io.novafoundation.nova.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMorePayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.controller.confirm.ConfirmSetControllerPayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.main.model.StakingStoryModel
import io.novafoundation.nova.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondPayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.rewardDestination.confirm.parcel.ConfirmRewardDestinationPayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondPayload
import io.novafoundation.nova.feature_staking_impl.presentation.validators.parcel.ValidatorDetailsParcelModel

interface StakingRouter {
    fun setResult(key:String,data:Any)
    fun setResultListener(key:String, listener: ResultListener)

    fun openSetupStaking()

    fun openStartChangeValidators()

    fun openRecommendedValidators()

    fun openSelectCustomValidators()

    fun openCustomValidatorsSettings()

    fun openSearchCustomValidators()

    fun openReviewCustomValidators()

    fun openValidatorDetails(validatorDetails: ValidatorDetailsParcelModel)

    fun openConfirmStaking()

    fun openConfirmNominations()

    fun returnToMain()

    fun openChangeAccount()

    fun openStory(story: StakingStoryModel)

    fun openPayouts()

    fun openPayoutDetails(payout: PendingPayoutParcelable)

    fun openConfirmPayout(payload: ConfirmPayoutPayload)

    fun openBondMore()

    fun openConfirmBondMore(payload: ConfirmBondMorePayload)

    fun openSelectUnbond()

    fun openConfirmUnbond(payload: ConfirmUnbondPayload)

    fun openRedeem()

    fun openControllerAccount()

    fun back()

    fun openConfirmSetController(payload: ConfirmSetControllerPayload)

    fun openCustomRebond()
    fun openConfirmRebond(payload: ConfirmRebondPayload)

    fun openCurrentValidators()

    fun returnToCurrentValidators()

    fun openChangeRewardDestination()

    fun openConfirmRewardDestination(payload: ConfirmRewardDestinationPayload)

    val currentStackEntryLifecycle: Lifecycle
}
