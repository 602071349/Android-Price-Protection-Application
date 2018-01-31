#File : getInfo.py
#=================
# This application gets the information form walmart api and then loads in json
# sends to the mobile - client side.

from flask import Flask
from flask import jsonify

import requests
import json



app=Flask(__name__)


# home directory
@app.route('/')
def home():
    return  "This is home of our application, Append /upc/BARCODE to check information !!!"

# our aws public domain is http://18.217.62.106/upc/035000521019,
# which can be used in any browser
# here 035000521019 can be any barcode, can be used right away
@app.route('/upc/<barcode>')  # URL with a variable
def single_query_item(barcode):
    #'http://api.walmartlabs.com/v1/items?apiKey='+api_key+'&upc='+barcode
    resp =  requests.get('http://api.walmartlabs.com/v1/items?apiKey=z6v36q8gwsyc9uk5pv3sbcdt&upc='+barcode)
    data = json.loads(resp.text)
    return jsonify(data)

if __name__ == "__main__":
    app.run()
