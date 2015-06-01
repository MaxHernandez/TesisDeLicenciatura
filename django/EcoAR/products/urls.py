from django.conf.urls import patterns, include, url
from views import ProductInfo, Commentaries

urlpatterns = patterns('',
    url(r'info/?$', ProductInfo.as_view(), name='product_info'),
    url(r'commentaries/?$', Commentaries.as_view(), name='product_commentaries'),
)
