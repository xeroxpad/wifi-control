package com.example.data.mappers

import com.example.data.entities.VKProfileInfoResponseDto
import com.example.domain.entities.ProfileInfo

class ProfileInfoMapper {
    fun mapResponseToProfileInfo(responseDto: VKProfileInfoResponseDto): ProfileInfo {
        return ProfileInfo(
            responseDto.response.firstName,
            responseDto.response.lastName,
            responseDto.response.avatarProfile
        )
    }
}