import urllib, time
import os.path
import sys
from bs4 import BeautifulSoup
import requests #git://github.com/kennethreitz/requests.git

GET  = 'get'
POST = 'post'

class WebParser (BeautifulSoup):
    
    def __init__(self, url, method, data = None):
        self.url = url
        self.raw_html = None
        if method == 'get':
            self.raw_html = self.send_get_request(data)
        elif method == 'post':
            self.raw_html = self.send_post_request(data)
        super(WebParser, self).__init__(self.raw_html)

    def send_get_request(self, params):
        if not params:
            return requests.get(self.url).text
        else:
            return requests.get(self.url, params=params).text

    def send_post_request(self, data):
        return requests.get(self.url, data=data).text

    def __string__(self):
        return self.raw_html

class AmazonParser(WebParser):
    
    def __init__(self, query):
        super(AmazonParser, self).__init__('http://www.amazon.com/s/', GET, {'field-keywords':query})
        self.parse_data()

    def parse_data(self):
        data = list()
        product_name_list = self.find_all('div', class_='rslt prod celwidget')
        for product in product_name_list:
            pdata = dict()
            pdata['product_name'] = product.find('span', class_='lrg bold').get_text()
            pdata['description'] = ""
            pdata['img_url'] = product.find('img')['src']
            pdata['shoping_service'] = "Amazon"
            pdata['product_url'] = product.find('div', class_='image imageContainer').find('a')['href']
            data.append(pdata)
            print pdata

def main():
    #url = 'http://elisa.dyndns-web.com/'
    #webparser = WebParser(url, GET)    
    AmazonParser(sys.argv[1])

if __name__ == '__main__':
    main()
