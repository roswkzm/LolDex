package com.example.loldex.core.data.repository

import com.example.loldex.core.common.network.Dispatcher
import com.example.loldex.core.common.network.LdDispatchers
import com.example.loldex.core.data.mapper.toData
import com.example.loldex.core.model.DisneyCharacterData
import com.example.loldex.core.network.DisneyNetworkDataSource
import com.example.loldex.core.network.model.ListResponse
import com.example.loldex.core.network.model.response.DisneyCharacterResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DisneyRepositoryImpl @Inject constructor(
    @Dispatcher(LdDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: DisneyNetworkDataSource
) : DisneyRepository {

    override fun getCharacter(
        page: Int,
        pageSize: Int
    ): Flow<ListResponse<List<DisneyCharacterResponse>>> =
        flow {
            val response = dataSource.getCharacter(page, pageSize)
            response.suspendOnSuccess {
                emit(data)
            }.onError {

            }.onException {

            }
        }.flowOn(ioDispatcher)

    override fun getCharacterById(id: Int): Flow<DisneyCharacterData> =
        flow {
            val response = dataSource.getCharacterById(id)
            response.suspendOnSuccess {
                emit(data.data.toData())
            }.onError {

            }.onException {

            }
        }.flowOn(ioDispatcher)

    override fun getCharacterByName(name: String): Flow<List<DisneyCharacterData>> =
        flow {
            val response = dataSource.getCharacterByName(name)
            response.suspendOnSuccess {
                emit(data.data.toData())
            }.onError {

            }.onException {

            }
        }.flowOn(ioDispatcher)

}