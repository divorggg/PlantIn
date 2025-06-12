package com.example.plantin.onboarding

import com.example.plantin.R

class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object {
        fun getData(): List<OnBoardingItems> {
            return listOf(
                OnBoardingItems(
                    R.drawable.img_onboarding1,
                    R.string.Welcome,
                    R.string.desc_onboarding1
                ),
                OnBoardingItems(
                    R.drawable.img_onboarding2,
                    R.string.none,
                    R.string.desc_onboarding2
                )

            )
        }
    }
}