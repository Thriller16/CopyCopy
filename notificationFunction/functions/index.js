'use strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Notifications/{user_id}/{notification_id}').onWrite((data, context) => {
  					const user_id = context.params.user_id;
					const notification = context.params.notification_id;

  console.log('User id is : ', user_id);
				
					const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');

					return deviceToken.then(result =>{
						const token_id = result.val();
						const payload = {
							notification: {
								title: "New Assignment",
								body: "You have a new assignment upload",
							}
						};
	
						return admin.messaging().sendToDevice(token_id, payload).catch((err) =>{
							console.log("err is ", err);
						})
					});
});