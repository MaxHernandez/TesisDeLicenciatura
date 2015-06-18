from django.conf.urls import patterns, include, url
from views import ProductInfo, Commentaries, CommentariesDelete, AddProduct

urlpatterns = patterns('',
    url(r'$', AddProduct.as_view(), name='product_view'),
    url(r'info/?$', ProductInfo.as_view(), name='product_info'),
    url(r'commentaries/?$', Commentaries.as_view(), name='product_commentaries'),
    url(r'commentaries/delete/?$', CommentariesDelete.as_view(), name='product_commentaries'),
)
