import os
import Image

def crop(img):
    im = Image.open(img)
    width, height = im.size                         # Get dimensions
    bottom = height/2                               # Get bottom half (to crop)
    im.crop((1, 1, width, bottom)).save(img)        # Crop

for path, subdirs, files in os.walk(os.getcwd()):   # Walks folders and subfolders
    for name in files:
        if name[-3:] == "png":                      # if PNG extension
            file = os.path.join(path, name)
            crop(file)
            print("Cropped: " + file)