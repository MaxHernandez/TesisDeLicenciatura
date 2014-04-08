import cv2, numpy
import camera
import features

def processing(frame, wframe):
    temp_feature = features.get_features(wframe)
    temp_descriptor = features.get_descriptor(wframe, temp_feature)
    
    points_list, patternimage_size = features.verify(temp_descriptor, temp_feature, 
                                                     #'/home/max/Pictures/logotipos/QR_Maxkalavera.png'
                                                     '/home/max/Pictures/logotipos/fime.jpg'
                                                     )

    if len(points_list[0]) >= 4 and len(points_list[1]) >= 4: 
        (h, m) = cv2.findHomography( numpy.array(points_list[1]), numpy.array(points_list[0]), cv2.RANSAC, ransacReprojThreshold=2.5)
        matches = m.ravel().tolist()

        wframe_size = wframe.shape 

        """
        wframe_rectsize = numpy.float32(
            [
                [0, 0],
                [0, wframe_size[0] - 1],
                [wframe_size[1] - 1, wframe_size[0] - 1],
                [wframe_size[1] - 1, 0]
                ]
            ).reshape(-1,1,2)
        wframe_rounding_box = cv2.perspectiveTransform(wframe_rectsize, h)
        """

        patternimage_rectsize = numpy.float32(
            [
                [0, 0],
                [0, patternimage_size[0] - 1],
                [patternimage_size[1] - 1, patternimage_size[0] - 1],
                [patternimage_size[1] - 1, 0]
                ]
            ).reshape(-1,1,2)
        wframe_rounding_box = cv2.perspectiveTransform(patternimage_rectsize, h)


        frame_size = frame.shape 

        ratio = [float(frame_size[0])/wframe_size[0], float(frame_size[1])/wframe_size[1]]

        frame_rounding_box = list()
        for cord in wframe_rounding_box:
            cord = cord[0]

            print wframe_size, cord, ratio            
            frame_rounding_box.append((
                    int(cord[0]*ratio[0]), int(cord[1]*ratio[1])
                    ))

        cv2.line(frame, 
                    frame_rounding_box[0],
                    frame_rounding_box[1],
                    (255, 0, 0), 1, 8 , 0);
        cv2.line(frame,
                    frame_rounding_box[1],
                    frame_rounding_box[2],
                    (255, 0, 0), 1, 8 , 0);
        cv2.line(frame, 
                    frame_rounding_box[2],
                    frame_rounding_box[3],
                    (255, 0, 0), 1, 8 , 0);
        cv2.line(frame, 
                    frame_rounding_box[3],
                    frame_rounding_box[0],
                    (255, 0, 0), 1, 8 , 0);

    
    #logo = features.verify_descriptor(temp_descriptor)

    #if logo != None:
    #    print logo

def main():
    cam = camera.CameraIterator(0)
    cam.start(processing, grayscale=True, size=(256, 256), workingCopy = True, show_frame=True, show_wframe=True)

if __name__ == '__main__':
    main()
