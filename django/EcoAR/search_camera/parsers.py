from rest_framework.parsers import BaseParser



class ImageJPGParser(BaseParser):

	media_type = 'image/jpg'

	def parse(self, stream, media_type=None, parser_context=None):
		print "-----> stream:", type(stream), "media_type:", type(media_type), "parser_context:", type(parser_context)



		return stream.read();