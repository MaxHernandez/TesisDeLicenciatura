import urllib, time
from bs4 import BeautifulSoup
import requests #git://github.com/kennethreitz/requests.git

GET  = 'get'
POST = 'post'

class WebParser (BeautifulSoup):
    
    def __init__(self, url, method, data = None):
        self.url = url
        self.raw_html = None
        if method == 'get':
            self.raw_html = self.send_get_request()
        elif method == 'post':
            self.raw_html = self.send_post_request(data)
        super(WebParser, self).__init__(self.raw_html)

    def send_get_request(self):
        return requests.get(self.url).text

    def send_post_request(self, data):
        return requests.get(self.url, data=data).text

    def __string__(self):
        return self.raw_html

class AmazonParser(WebParser):
    
    def __init__(self, query):
        super(AmazonParser, self).__init__('http://www.amazon.com/s/', POST, {'field-keywords':query})
        self.parse_data()

    def parse_data(self):
        print self.raw_html
        product_name_list = self.find_all('span', class_='lrg bold')
        #print product_name_list

class EjemploWC(WebParser):

    def comp_class(self, tag, val, attr='class'):
        if tag.has_attr(attr) and tag.attrs[attr][0] == val:
            return True
        else:
            return False

    def get_attr(self, tag, attr):
        if tag.has_attr(attr):
            return tag.attrs[attr]
        else:
            return None

    def parse_data(self):
        submenus_url_list = self.get_submenus()
        print len(submenus_url_list), submenus_url_list
        #products_url_list = self.get_products(submenus_url_list)

    def get_products(self, smlist):
        for url in sllist:
            web_parser = WebParser(url)
            
            time.sleep(0.1)

    def get_submenus(self):
        # Main Menu 
        submenus_url_list = list()
        query = self.find_all('li')
        for i in range(len(query)):
             if self.comp_class(query[i], 'vineta_punto') and self.comp_class(query[i+1], 'has-sub'):
                submenus_url_list.append(''+self.get_attr(query[i+1].a, 'href'))
        return submenus_url_list


def main():
    #url = 'http://elisa.dyndns-web.com/'
    #webparser = WebParser(url, GET)    
    AmazonParser('Car')

if __name__ == '__main__':
    main()
