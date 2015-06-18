from django.conf.urls import patterns, include, url
from rest_framework.urlpatterns import format_suffix_patterns

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()
from general.views import SignUp, LogIn, LogOut, CheckSession, Session
from search_camera.views import BrandsView, BrandInfo, Commentaries
from products.views import CommentariesDelete

urlpatterns = patterns('',
    url(r'^session/?$', Session.as_view(), name='session'),
    url(r'^csession/?$', CheckSession.as_view(), name='checkSession'),
    url(r'^signup/?$', SignUp.as_view(), name='signUp'),
    url(r'^login/?$', LogIn.as_view(), name='logIn'),
    url(r'^logout/?$', LogOut.as_view(), name='logOut'),
    
    url(r'^brands/?$', BrandsView.as_view(), name='brands'),
    url(r'^brands/info/?$', BrandInfo.as_view(), name='brand_info'),
    url(r'^brands/commentaries/?$', BrandCommentaries.as_view(), name='brand_commentaries'),
    url(r'^brands/commentaries/delete/?$', CommentariesDelete.as_view(), name='brand_commentaries_delete'),

    url(r'^products/', include('products.urls')),
    url(r'^searchcam/', include('search_camera.urls')),
)

urlpatterns = format_suffix_patterns(urlpatterns)
