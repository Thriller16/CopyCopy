'use strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Notifications/{user_id}/{notification_id}').onWrite((data, context) => {
  					const user_id = context.params.user_id;
					const notification_id = context.params.notification_id;

  console.log('User id is : ', user_id);
  console.log('Notification id is : ', notification_id);
  
  
  const courseName = admin.database().ref(`/Notifications/${user_id}/${notification_id}`).once('value');

  return courseName.then(courseNameResult =>{

	  const course_title = courseNameResult.val().title;
	  const course_message = courseNameResult.val().message;
	  
	// return console.log("The course title is" , course_title, "And the message is " , course_message);
	
	const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');

	return deviceToken.then(result =>{
		const token_id = result.val();
		const payload = {
			notification: {
				title: course_title,
				body: course_message,
				icon: "default",
				click_action: "com.lawrene.falcon.copycopy_TARGET_NOTIFICATION"
			}
		};

		return admin.messaging().sendToDevice(token_id, payload).catch((err) =>{
			console.log("err is ", err);
		})
	});
  });
});
