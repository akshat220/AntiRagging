const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.newPost = functions.firestore
    .document("Posts/{documentId}")
    .onCreate((snapshot, context) => {
        const newValue = snapshot.data();
        pushMessage("There is a new Post. Check it out.", "Notification");
    });

function pushMessage(message, userid) {
    var payload = {
        notification: {
            body: message,
        }
    };
    admin.messaging().sendToTopic(userid, payload)
        .then(function(response) {
            console.log("Successfully sent message:", response);
            return true;
        })
        .catch(function(error) {
            console.log("Error sending message:", error);
        });
}
