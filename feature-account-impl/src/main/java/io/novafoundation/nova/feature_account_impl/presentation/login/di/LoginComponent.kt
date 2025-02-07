package io.novafoundation.nova.feature_account_impl.presentation.login.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.login.LoginFragment

@Subcomponent()
@ScreenScope
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): LoginComponent
    }

    fun inject(fragment: LoginFragment)
}
