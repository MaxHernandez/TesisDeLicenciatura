from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    url(r'^$', 'searchProduct.views.search', name='searchProduct'),                       
    url(r'^search/$', 'searchProduct.views.search', name='searchProduct'),
    # Examples:
    # url(r'^$', 'EcoAR.views.home', name='home'),
    # url(r'^EcoAR/', include('EcoAR.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
)
