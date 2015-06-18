from django.conf.urls import patterns, include, url
from views import SearchCamera

urlpatterns = patterns('',
    url(r'$', SearchCamera.as_view(), name='search_camera'),
)

