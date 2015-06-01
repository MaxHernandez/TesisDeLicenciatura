from django.conf.urls import patterns, include, url
from rest_framework.urlpatterns import format_suffix_patterns

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()
from general.views import SignUp, LogIn, LogOut, CheckSession, Session

urlpatterns = patterns('',
    url(r'^session/?$', Session.as_view(), name='session'),
    url(r'^csession/?$', CheckSession.as_view(), name='checkSession'),
    url(r'^signup/?$', SignUp.as_view(), name='signUp'),
    url(r'^login/?$', LogIn.as_view(), name='logIn'),
    url(r'^logout/?$', LogOut.as_view(), name='logOut'),
    
    url(r'^products/', include('products.urls')),
)

urlpatterns = format_suffix_patterns(urlpatterns)
