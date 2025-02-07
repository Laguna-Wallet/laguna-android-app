package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.SeedPromptFragment

@Subcomponent(
    modules = [
        SeedPromptModule::class
    ]
)
@ScreenScope
interface SeedPromptComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance isAuth: AddAccountPayload,
        ): SeedPromptComponent
    }

    fun inject(fragment: SeedPromptFragment)
}
