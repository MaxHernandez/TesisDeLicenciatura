from django.conf.urls import patterns, include, url

from rest_framework.urlpatterns import format_suffix_patterns

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

from searchProduct.views import SearchBarView

urlpatterns = patterns('',
    url(r'^$', SearchBarView.as_view(), name='searchProduct'),                       
    url(r'^search/$', SearchBarView.as_view(), name='searchProduct'),
    # Examples:
    # url(r'^$', 'EcoAR.views.home', name='home'),
    # url(r'^EcoAR/', include('EcoAR.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
)

urlpatterns = format_suffix_patterns(urlpatterns)
