package io.novafoundation.nova.feature_account_impl.di

import android.content.Context
import coil.ImageLoader
import com.google.gson.Gson
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.data.network.AppLinksProvider
import io.novafoundation.nova.common.data.network.rpc.SocketSingleRequestExecutor
import io.novafoundation.nova.common.data.secrets.v1.SecretStoreV1
import io.novafoundation.nova.common.data.secrets.v2.SecretStoreV2
import io.novafoundation.nova.common.data.storage.Preferences
import io.novafoundation.nova.common.data.storage.encrypt.EncryptedPreferences
import io.novafoundation.nova.common.di.modules.Caching
import io.novafoundation.nova.common.resources.AppVersionProvider
import io.novafoundation.nova.common.resources.ClipboardManager
import io.novafoundation.nova.common.resources.LanguagesHolder
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.systemCall.SystemCallExecutor
import io.novafoundation.nova.common.validation.ValidationExecutor
import io.novafoundation.nova.common.vibration.DeviceVibrator
import io.novafoundation.nova.core_db.dao.AccountDao
import io.novafoundation.nova.core_db.dao.ContactsDao
import io.novafoundation.nova.core_db.dao.MetaAccountDao
import io.novafoundation.nova.core_db.dao.NodeDao
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicBuilderFactory
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import io.novafoundation.nova.runtime.network.rpc.RpcCalls
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import java.util.Random

interface AccountFeatureDependencies {

    fun appLinksProvider(): AppLinksProvider

    fun preferences(): Preferences

    fun encryptedPreferences(): EncryptedPreferences

    fun resourceManager(): ResourceManager

    fun iconGenerator(): IconGenerator

    fun clipboardManager(): ClipboardManager

    fun context(): Context

    fun deviceVibrator(): DeviceVibrator

    fun userDao(): AccountDao
    fun contactsDao():ContactsDao
    fun nodeDao(): NodeDao

    fun languagesHolder(): LanguagesHolder

    fun socketSingleRequestExecutor(): SocketSingleRequestExecutor

    fun jsonMapper(): Gson

    fun addressIconGenerator(): AddressIconGenerator

    @Caching
    fun cachingIconGenerator(): AddressIconGenerator

    fun random(): Random

    fun secretStoreV1(): SecretStoreV1

    fun secretStoreV2(): SecretStoreV2

    fun metaAccountDao(): MetaAccountDao

    fun chainRegistry(): ChainRegistry

    fun extrinsicBuilderFactory(): ExtrinsicBuilderFactory

    fun rpcCalls(): RpcCalls

    fun imageLoader(): ImageLoader

    fun appVersionProvider(): AppVersionProvider

    fun validationExecutor(): ValidationExecutor

    val systemCallExecutor: SystemCallExecutor

    val multiChainQrSharingFactory: MultiChainQrSharingFactory
}
