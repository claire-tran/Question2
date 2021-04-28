# Question2_CIC

## Overview
- A library using Nexmo SMS API to send a OTP message contains random 6 digits code to a phone number 
- Will check if daily OTP sms exceeded
- Return error
- Retry when fail

## Main idea
- Using a file to track the information of daily sms and delete unnecessary data from the day before in order to maintain simple way to track data
- Write back to file when sent new sms

## How To Use
- Example
> Repository repository = new Repository(); </br>
SMSSender sender = new SMSSender(apiKey, apiSecret, repository, maxRetry, maxDailyOTP, maxDailyOTPForANumber); </br>
sender.sendOTP(senderName, receiverPhoneNumber);

- using this code in the main function with :
- apiKey and apiSecret is provided by Nexmo(use apiKey "7d79bbaa", apiSecret "nMvG95ZsqOjQ3dAO" to try)
- reposity: the repo just create for handle write, read data to file
- maxRetry: times to retry if fails to send sms
- maxDailyOTP: max sms can be sent in one day
- maxDailyOTPForANumber: max sms can be sent in one day to a phone number
- senderName: your name
- receiverPhoneNumber: phone number of the receiver
