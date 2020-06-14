import urllib.request
import os

def download_images():
	# https://openweathermap.org/weather-conditions
	url = "http://openweathermap.org/img/w/{}.png"

	for i in [1,2,3,4,9,10,11,13,50]:
		
		name = str(i).zfill(2) + "d"
		urllib.request.urlretrieve( url.format(name), "w_{}.png".format(name) )

		name = str(i).zfill(2) + "n"
		urllib.request.urlretrieve( url.format(name), "w_{}.png".format(name) )


def print_image_names():
	names = []
	for i in os.listdir(os.getcwd()):
		if i.startswith("w_"):
			names.append(i)

	java_names = ''
	r_ids = ''
	for i in names:
		java_names += '"{}", '.format(i[2:5])
		r_ids += "R.drawable.{}, ".format(i[:5].replace("n", "d"))
	
	print(r_ids)

print_image_names()