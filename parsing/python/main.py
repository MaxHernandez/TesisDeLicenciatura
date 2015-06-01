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

"""
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
"""

class AmazonParser(WebParser):

    SHOPING_SERVICE_INDEX = "AMZN"
    GENERAL_ID_NUMBER_CHARS = 16

    def __init__(self, query):
        super(AmazonParser, self).__init__('http://www.amazon.com/s/', GET, {'field-keywords':query})
        self.parse_data()

    def build_general_id(self, string):
        if len(string) > self.GENERAL_ID_NUMBER_CHARS:
            print "Error, id no soportado"
            return ''

        output = ''
        for i in range(self.GENERAL_ID_NUMBER_CHARS - len(string)):
            output += '0'
        return self.SHOPING_SERVICE_INDEX + output + string

    def parse_data(self):
        data = list()

        product_name_list = self.find_all('div', class_='s-item-container')
        print len(product_name_list)

        for product in product_name_list:
            #print "Product:"
            #print product 
            #print product.find('a', class_='a-link-normal s-access-detail-page a-text-normal').find('h2').get_text()
            #print product.find('img', alt='Product Details')
            pdata = dict()
            # name
            aTags = product.findAll('a')
            for a in aTags:
                if a.has_attr('title'):
                    pdata['name'] = a.get_text()
                    pdata['url'] = a['href']
                    # 15 Caracteres - Mas tres de un identificador de la tienda
                    pdata['general_id'] = self.build_general_id( pdata['url'].split('/')[-1] )
            pdata['shoping_service'] = "Amazon"
            pdata['img'] = product.find('img')['src']


            try:
                description = product.find('span', text='Product Features')
                description_parent = description.find_parent()
                pdata['description'] = description_parent.find_all('span')[-1].get_text()
            except:
                pdata['description'] = None
            #print description_parent

            data.append(pdata)
                
        for d in data:
            print d
        

def main():
    #url = 'http://elisa.dyndns-web.com/'
    #webparser = WebParser(url, GET)    
    AmazonParser(sys.argv[1])

if __name__ == '__main__':
    main()
