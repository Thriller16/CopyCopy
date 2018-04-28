'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Notifications/{user_id}/{notification_id}').onWrite((data, context) => {
					const uid = context.params.user_id;
					const notification = context.params.notification_id;

					console.log("Notification is to be sent to: ", uid);
				
					if(context.data.val()){
						return console.log("Notification " , notification, " has been deleted");
					}


					// const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');

					// return deviceToken.then(result =>{
					// 	const token_id = result.val();

					// 	console.log("THe id is ", token_id);

					// 	const payload = {
					// 		notification: {
					// 			title: "New Post",
					// 			body: "You have received a new notification",
					// 			icon: "default"
					// 		}
					// 	};
	
					// 	return admin.messaging().sendToDevice(token_id, payload).then(response =>{
					// 		return console.log("This was the notificTION FEATRURE");
					// 	});
					// });		
});
