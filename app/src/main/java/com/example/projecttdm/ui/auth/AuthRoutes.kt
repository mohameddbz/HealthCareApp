package com.example.projecttdm.ui.auth

sealed class AuthRoutes  (val route : String){
    object splash : AuthRoutes("splash")
    object onboarding : AuthRoutes("onboarding")
    object onboarding2 : AuthRoutes("onboarding2")
    object onboarding3 : AuthRoutes("onboarding3")
    object welcomScreen : AuthRoutes("welcomScreen")
    object loginScreen : AuthRoutes("login")
    object registerScreen : AuthRoutes("register")
    object profileScreen : AuthRoutes("profile")

}