# message-dump
An app that I use to get all messages I've sent to a particular phone number
`FileWorker.init(context: Context)`
It has functions that can:
<br/>
Fetch all phone messages
<br/>
FileWorker.getAllSentSms(null)`
<br/>
`FileWorker.dumpAllSms(null)`
<br/>

Fetch or save messages sent to numbers that match a regex pattern
<br/>
`FileWorker.getAllSentSms(number: String)`
<br/>
`FileWorker.dumpAllSms(number: String)`
<br/>

Filter messages and get all messages sorted in a list.
<br/>
`FileWorker.dumpFilteredSms(number: String`
<br/>

Generate a pdf of all the messages
<br/>
`FileWorker.generateSmsPdf(number: String)`
<br/>
The use cases are limitless,
<br/>
You can clone this repository and make your own modifications to suite your needs.
<br/>

## NOTE
<br/>
This repo will not be maintained and will not bne accepting PRs