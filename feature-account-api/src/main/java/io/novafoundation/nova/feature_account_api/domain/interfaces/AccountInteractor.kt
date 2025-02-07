package io.novafoundation.nova.feature_account_api.domain.interfaces

import io.novafoundation.nova.common.data.model.Contact
import io.novafoundation.nova.common.data.model.ContactUiMarker
import io.novafoundation.nova.core.model.CryptoType
import io.novafoundation.nova.core.model.Language
import io.novafoundation.nova.core.model.Node
import io.novafoundation.nova.feature_account_api.domain.model.Account
import io.novafoundation.nova.feature_account_api.domain.model.LightMetaAccount
import io.novafoundation.nova.feature_account_api.domain.model.MetaAccount
import io.novafoundation.nova.feature_account_api.domain.model.PreferredCryptoType
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import kotlinx.coroutines.flow.Flow

interface AccountInteractor {

    suspend fun generateMnemonic(): Mnemonic

    fun getCryptoTypes(): List<CryptoType>
    suspend fun getPreferredCryptoType(chainId: ChainId? = null): PreferredCryptoType

    suspend fun isCodeSet(): Boolean

    suspend fun savePin(code: String)

    suspend fun isPinCorrect(code: String): Boolean

    suspend fun isBiometricEnabled(): Boolean

    suspend fun setBiometricOn()

    suspend fun setBiometricOff()
    suspend fun saveContact(data: Contact)
    fun getContacts(): Flow<List<ContactUiMarker>>
    suspend fun deleteContact(id: String)
    suspend fun getMetaAccount(metaId: Long): MetaAccount

    suspend fun updateMetaAccountName(metaId: Long, name: String)
    suspend fun updateMetaAccountIcon(metaId: Long, resOrPath: String)

    fun lightMetaAccountsFlow(): Flow<List<LightMetaAccount>>

    fun selectedMetaAccountFlow(): Flow<MetaAccount>

    fun getAutoLockTimer(): Flow<String>
    suspend fun saveAutoLockTimer(data: String)
    suspend fun selectMetaAccount(metaId: Long)
    suspend fun updateMetaAccountAvatar(metaId: Long, avatar: String)
    suspend fun deleteAccount(metaId: Long)

    suspend fun updateMetaAccountPositions(idsInNewOrder: List<Long>)

    fun nodesFlow(): Flow<List<Node>>

    suspend fun getNode(nodeId: Int): Node

    fun getLanguages(): List<Language>

    suspend fun getSelectedLanguage(): Language

    suspend fun changeSelectedLanguage(language: Language)

    suspend fun addNode(nodeName: String, nodeHost: String): Result<Unit>

    suspend fun updateNode(nodeId: Int, newName: String, newHost: String): Result<Unit>

    suspend fun getAccountsByNetworkTypeWithSelectedNode(networkType: Node.NetworkType): Pair<List<Account>, Node>

    suspend fun selectNodeAndAccount(nodeId: Int, accountAddress: String)

    suspend fun selectNode(nodeId: Int)

    suspend fun deleteNode(nodeId: Int)
}
