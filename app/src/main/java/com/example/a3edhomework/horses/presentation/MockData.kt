package com.example.a3edhomework.horses.presentation

import com.example.a3edhomework.horses.presentation.model.HorseUIModel

object MockData {
    fun getHorses(): List<HorseUIModel> = listOf(
        HorseUIModel(
            id = "1",
            name = "Deep Impact",
            owner = "Makoto Kaneko",
            majorWin = "Tokyo Yushun (Japanese Derby)",
            earnings = "¥1,455,551,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/ca/Deep_Impact%28horse%29_20051023_3.jpg"
        ),
        HorseUIModel(
            id = "2",
            name = "Kitasan Black",
            owner = "Ono Shoji",
            majorWin = "Japan Cup",
            earnings = "¥1,876,843,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/Kitasan-Black_IMG_7729r_R_20151025.JPG/1200px-Kitasan-Black_IMG_7729r_R_20151025.JPG"
        ),
        HorseUIModel(
            id = "3",
            name = "Orfevre",
            owner = "Sunday Racing Co. Ltd.",
            majorWin = "Arima Kinen",
            earnings = "¥1,576,264,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/3b/Orfevre20111225%282%29.jpg"
        ),
        HorseUIModel(
            id = "4",
            name = "Gentildonna",
            owner = "Sunday Racing Co. Ltd.",
            majorWin = "Japan Cup",
            earnings = "¥1,750,000,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/1d/Gentildonna_Arima_Kinen_2014%28IMG2%29.jpg"
        ),
        HorseUIModel(
            id = "5",
            name = "Satono Diamond",
            owner = "Satomi Hajime",
            majorWin = "Kikka Sho",
            earnings = "¥800,000,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/5/50/Satono_Diamond_IMG_9079-1_20161023.jpg"
        ),
        HorseUIModel(
            id = "6",
            name = "Almond Eye",
            owner = "Silk Racing Co. Ltd.",
            majorWin = "Japan Cup",
            earnings = "¥1,890,000,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Almond_Eye_Yushun_Himba_2018%28IMG1%29.jpg/1200px-Almond_Eye_Yushun_Himba_2018%28IMG1%29.jpg"
        ),
        HorseUIModel(
            id = "7",
            name = "Contrail",
            owner = "Shinji Maeda",
            majorWin = "Tokyo Yushun (Japanese Derby)",
            earnings = "¥1,250,000,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Contrail_2019_hopeful_stakes_honbaba_nyujyou.jpg/1200px-Contrail_2019_hopeful_stakes_honbaba_nyujyou.jpg"
        ),
        HorseUIModel(
            id = "8",
            name = "Symboli Kris S",
            owner = "Symboli Farm",
            majorWin = "Arima Kinen",
            earnings = "¥984,000,000",
            imageUrl = "https://www.racingpost.com/_next/image/?url=https%3A%2F%2Fs3-eu-west-1.amazonaws.com%2Fprod-media-racingpost%2Fprod%2Fimages%2F169_1008%2F10dd69d03a3e-105426.jpg&w=3840&q=75"
        ),
        HorseUIModel(
            id = "9",
            name = "Vodka",
            owner = "Tanaka Yuzo",
            majorWin = "Japan Cup",
            earnings = "¥1,300,000,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/15/Vodka%28horse%29_20070527R1.jpg"
        ),
        HorseUIModel(
            id = "10",
            name = "Duramente",
            owner = "Sunday Racing Co. Ltd.",
            majorWin = "Tokyo Yushun (Japanese Derby)",
            earnings = "¥670,000,000",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Duramente_Tokyo_Yushun_2015%28IMG1%29.jpg/1200px-Duramente_Tokyo_Yushun_2015%28IMG1%29.jpg"
        )
    )
}