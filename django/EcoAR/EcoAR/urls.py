from django.conf.urls import patterns, include, url
from rest_framework.urlpatterns import format_suffix_patterns

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

from searchProduct.views import SearchBarView
from general.views import SignUp, LogIn, LogOut, CheckSession

urlpatterns = patterns('',
    url(r'^$', SearchBarView.as_view(), name='searchProduct'),
    url(r'^search/?$', SearchBarView.as_view(), name='searchProduct'),
    url(r'^signup/?$', SignUp.as_view(), name='signUp'),
    url(r'^login/?$', LogIn.as_view(), name='logIn'),
    url(r'^logout/?$', LogOut.as_view(), name='logOut'),
    url(r'^csession/?$', CheckSession.as_view(), name='checkSession'),
)

urlpatterns = format_suffix_patterns(urlpatterns)
