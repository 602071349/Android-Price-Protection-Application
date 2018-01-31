1.This app is designed to allow users to get price proection from their credit card company.We use OCR api, barcode api, walmart open api, firebase,AWS and so on. The app will periodically check the product price in walmart database and compare itto the product price in our firebase. If there is a significant price change, we will send notification to users.


2.We have implemented email verfication in our app, so you do need to verify before login. 

(click the "+" button on input page to start OCR or barcode scanner)
3.For OCR. you need to drag the rectangle box, to make sure it contains only the product name and product price. Then press the camera button.Those two values can be catched and show up on the input page.

4.For barcode scanner, it a little demanding. Barcode input will only allow merchandise that are sold by Walmart, So every other products' barcode would not work except Walmart's own product.

5.The server code requires lot of dependencies. Server code is not necessary to be run while evaluating app. It is being run in our AWS server through (CRONTAB). It is just here for code evaluation purpose. This also contain middleware that connects AWS to Firebase.
product.py goes to firebase and gets all the information and checks for any modification. If there is change then it will send the notification to the user.




priceapp-c61a6-firebase-adminsdk-dydkz-38de89e69d is the key to access our database. This application requires this. 


getInfo.py 
Flask application that routes calls from client to our server to WALMART API
Done in order to hide api key and also to add new api in our server side code without changing client side code later down the line. 
Public Domain :http://18.217.62.106/upc/035000521019 which is being hosted at our AWS server, in which 035000521019 represents barcode, http://18.217.62.106/upc/<ANY_BARCODE>









6.Make sure you have Google Play Services installed and open on your device.


7.We achieved following optional Bonuses:
	a.Using Menus with icons.
	b.Using Gestures and / or Acelerometer. (You can fling to delete items in home page£©
	c.Targeting multiple locales. (You can change your device language to Chinese, and the whole app will be in Chinese)
	
8.Because the ppt website we use requires membership to export ppt, we screen shot all slides in slides folder.