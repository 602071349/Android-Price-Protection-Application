# File : Product.py
# =================
# This code runs in our server (6 hours periodically) -- using crontab
# Application: Gets all the firebase info and checks whether price changed or not.
# If true : Send push notification and email notification

import json
import requests
from pprint import pprint


import csv
import time


import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from firebase import firebase


# Import smtplib for the actual sending function
# Import the email modules we'll need
import smtplib
from email.mime.text import MIMEText
from email.MIMEMultipart import MIMEMultipart


# key for FCM push notificaiton
from pyfcm import FCMNotification
api_fcm_key = "AAAA4_hQFKQ:APA91bFhF6SbOtJs3hCGHTXXxevXUUEPje0R2yueJIwChZwxmq68R8jBHbKS8j_5c_opArcba80NCiTWN6VTIV5ZA42EW_3am6HYjiQaNMM1jvymPw_VONMSCb4gDJHYuBVvb_JoI6yp"

#fcm
push_service = FCMNotification(api_key= api_fcm_key)

print(push_service)

# walmart api for searching product
api_key ='z6v36q8gwsyc9uk5pv3sbcdt'
#resp =  requests.get('http://api.walmartlabs.com/v1/items?id=12417832&apiKey='+api_key)

# firebase config
cred = credentials.Certificate('/priceapp-c61a6-firebase-adminsdk-dydkz-38de89e69d.json')

firebase_admin.initialize_app(cred, {
                              'databaseURL': 'https://priceapp-c61a6.firebaseio.com/'
                              })


root = db.reference()
products = root.child('Products')
Users = root.child('Users')


# helper function
# remove
def decimal(val):
    value_arr = val.strip("$")
    dollar_dec = float(value_arr)
    return dollar_dec


# Open a plain text file for reading.  For this example, assume that
# the text file contains only ASCII characters.
fw = open('textfile', 'w')
fp = open('textfile', 'rb')
# Create a text/plain message
msg = MIMEMultipart()
#msg = MIMEText(fp.read())

fp.close()
fw.close()

# me == the sender's email address
# you == the recipient's email address

####################################################################
#api_key ='z6v36q8gwsyc9uk5pv3sbcdt'
ID_LIST = ['896074146','19336123']

# config for file
fd = open('document.csv','w')

# for multiple queries
fRead = open('document.csv','r')

#helper function
def array_to_string(list):
    changed_value = ','.join(list)
    return changed_value

#reading csv File
def read_file(values,search_itemId):
    for value in values:
        array_value = value[:-1].split(',')
        if array_value[0] == search_itemId:
            price = array_value[1]
            return "$"+price
    return "Error"

# length of the file
def get_length():
    count = 0
    for value in fRead:
        count+=1
    return count


def update():
    length = get_length()
    print(length)


#
def checkChange(new_price, old_price):
    percent_change = (new_price / old_price) * 100
    if percent_change >= 10:
        return True
    else:
        return False

# push notification
def sendPushMessage(registration_id,name,date,original,bestPrice,url,barcode,image):
    
    registration_id = registration_id
    message_title = "Price Update Notification"
    
#input values here
    data = {"score":"3x1",
"name":str(name),
"date":str(date),
"original":str(original),
"BestPrice":str(bestPrice),
"url":str(url),
"barcode":str(barcode),
"image":str(image)
}
    
    message_body = "Price of one of the item has changed!!"
    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body,data_message = data)
    print(result)

##########################################################################
# returns price of particular data from api
def single_query_item(barcode):
    'http://api.walmartlabs.com/v1/items?apiKey='+api_key+'&upc='+barcode
    resp =  requests.get('http://api.walmartlabs.com/v1/items?apiKey='+api_key+'&upc='+barcode)
    data = json.loads(resp.text)

    itemId = data["items"][0]["itemId"]
    price = data["items"][0]["salePrice"]
    upc = data["items"][0]["upc"]
    url = data["items"][0]["productUrl"]
    
    myCsvRow = str(itemId)+','+ str(price)+','+str(upc)+'\n'
    fd.write(myCsvRow)
    time.sleep(1.4)
    return "$"+str(price)


# for future use 
def multiple_queries_items(id_list):
    length = len(id_list)
    ids = array_to_string(id_list)
    payload = {'ids': ids, 'apiKey':'z6v36q8gwsyc9uk5pv3sbcdt'}
    resp = requests.get('http://api.walmartlabs.com/v1/items?',payload)
    
    data = json.loads(resp.text)
    
    for i in range(0,length):
        itemId = data["items"][i]["itemId"]
        price = data["items"][i]["salePrice"]
        upc = data["items"][i]["upc"]
        myCsvRow = str(itemId)+','+ str(price)+','+str(upc)+'\n'
        fd.write(myCsvRow)

##########################################################################
# parsing all the database for new value
# send push and mail notification
# products <- one of child node from our database which is live right now
for users in products.get():
    users_list = products.child(users)
    for product in users_list.get():
        
        detail_list = users_list.child(product)
        barcode = detail_list.child('barcode').get() # barcode
        original_price = detail_list.child('price').get() # old price
        name = detail_list.child('name').get()  #name
        image = detail_list.child('image').get() #image
        url = detail_list.child('url').get()
        date = detail_list.child('date').get()
        best_price = detail_list.child('bestprice') # best price
        bestPrice = detail_list.child('bestprice').get()
        
        #get all values from here
        value = single_query_item(barcode)
        
        new_price = decimal(value)
        old_price = decimal(original_price)
        per = (old_price - new_price)/old_price

        # obtaining email from the firebase db
        if per >= 0.10 :
            
            
            # email notification
            msg = MIMEMultipart()
            msg['To'] = Users.child(users).child('Email').get()
            print(Users.child(users).child('Email').get())
            msg['Subject'] = 'Price Changed !!!!!'
            msg['From'] = 'no-reply@priceprotection.com'
            print("Price Decreased")
            body = 'Name:{} \n Date:{} \n Original Price:{} \n Best Price:{} \n URL:{} \n Barcode:{} '.format(str(name),str(date),str(original_price),str(bestPrice),str(url),str(barcode))
            print(body)
            s = smtplib.SMTP('localhost')
            
            # sending push notification
            registration_id = Users.child(users).child('messaging_token').get()
            # all field here

            # push notificaiton
            sendPushMessage(registration_id,name,date,original_price,bestPrice,url,barcode,image)
            # sending email notification
            msg.attach(MIMEText(body, 'plain'))
            s.sendmail(msg['From'],msg['To'], msg.as_string())
            detail_list.update({ 'bestprice':"$"+str(new_price)})

fd.close()
s.quit()
fRead.close()







