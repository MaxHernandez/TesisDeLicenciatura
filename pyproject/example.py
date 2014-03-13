import cv2, numpy
import camera
import features

def processing(frame, wframe):
    temp_feature = features.get_features(wframe)
    temp_descriptor = features.get_descriptor(wframe, temp_feature)
    
    points_list = features.verify(temp_descriptor, temp_feature, '/home/max/Pictures/logotipos/QR_Maxkalavera.png')

    print len(points_list)

    cv2.findHomography( numpy.array(points_list[0]), numpy.array(points_list[1]), cv2.RANSAC)
    

    #logo = features.verify_descriptor(temp_descriptor)

    #if logo != None:
    #    print logo

def main():
    cam = camera.CameraIterator(0)
    cam.start(processing, grayscale=True, size=(256, 256), workingCopy = True, show_frame=True, show_wframe=True)

if __name__ == '__main__':
    main()
