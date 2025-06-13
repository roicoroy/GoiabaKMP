package com.goiaba.data.networking

var strapiAccessToken = "603e9bf85124b9f6357b2c498f2e14ac4e877d7b137bf013562deabf8dbba745f7f25b516c71a1b880846b846857346a8e88f62a030f0553112a68f5b4f28305356cb1a2e8a1a706f90f9efff713adaaa78a9f520910a5dd9583cace0249984832a1567e64d66663a8addcbcd316359268cbfad8e104d505cd8a3ae07744bb8b"
var strapiUrl = "https://f497-2a00-23c7-dc88-f401-14ae-b5fa-413c-3803.ngrok-free.app/"
var populatePosts = "api/posts?populate=*"
var posts = "api/posts/"
// AUTH
var loginRequest = "api/auth/local"
var registerRequest = "api/auth/local/register"
// UPLOAD
var uploadFiles = "api/upload" // form data with files:formdata()
var getAllUploadFiles = "api/upload/files"
// LOGGER
var getAllLoggers = "/api/cow-loggers?populate=*"
var getOneLogger = "/api/cow-loggers/"
// Profile
var apiUsersMe = "api/users/me?populate=*"