# message-dump
An android app that can be used to:
<br/>
get all messages sent or received from a particular phone number.
<br/>
Generate a PDF document from the messages
<br/>
Save messages in json to phone storage
<br/>

Initialize the Worker class first
<br/>
`FileWorker.init(context: Context)`
<br/>
It has functions that can:
<br/>
Fetch all phone messages
<br/>
`FileWorker.getAllSentSms(null)`
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
`FileWorker.dumpFilteredSms(number: String)`
<br/>

Generate a PDF document of all the messages
<br/>
`FileWorker.generateSmsPdf(number: String)`
<br/>
The use cases are limitless,
<br/>
You can clone this repository and make your own modifications to suite your needs.
<br/>

## NOTE
<br/>
This repo will not be maintained and will not be accepting PRs