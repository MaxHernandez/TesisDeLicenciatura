import cv2, numpy
import camera
import features

def processing(frame, wframe):
    temp_feature = features.get_features(wframe)
    temp_descriptor = features.get_descriptor(wframe, temp_feature)
    
    points_list, patternimage_size = features.verify(temp_descriptor, temp_feature,
                                                     #'/home/max/Pictures/logotipos/QR_Maxkalavera.png'
                                                     '/home/max/Pictures/logotipos/fime.jpg'
                                                     #'/home/max/Pictures/logotipos/logo006.jpg'
                                                     )

    wframe_size = wframe.shape 
    frame_size = frame.shape 
    ratio = [float(frame_size[0])/wframe_size[0], float(frame_size[1])/wframe_size[1]]

    print "Number of points:", len(points_list[0])
    if len(points_list[0]) >= 10: 

        (h, m) = cv2.findHomography( numpy.array(points_list[1]), numpy.array(points_list[0]), cv2.RANSAC,  ransacReprojThreshold = 3.0)
        matches = m.ravel().tolist()

        if True:
            for i in range(len(points_list[0])):
                pt = points_list[0][i]

                if matches[i] > 0:
                    cv2.circle(frame, (int(pt[0]*ratio[0]), int(pt[1]*ratio[1])), 3, (255,0,255), -1)
                else:
                    cv2.circle(frame, (int(pt[0]*ratio[0]), int(pt[1]*ratio[1])), 3, (0, 255,255), -1)

        patternimage_rectsize = numpy.float32(
            [
                [0, 0],
                [0, patternimage_size[0] - 1],
                [patternimage_size[1] - 1, patternimage_size[0] - 1],
                [patternimage_size[1] - 1, 0]
                ]
            ).reshape(-1,1,2)

        wframe_rounding_box = cv2.perspectiveTransform(patternimage_rectsize, h)

        frame_rounding_box = list()
        for cord in wframe_rounding_box:
            cord = cord[0]

            frame_rounding_box.append((
                    int(cord[0]*ratio[0]), int(cord[1]*ratio[1])
                    ))

        if False:
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

        x_pts = [x for x, y in frame_rounding_box]
        y_pts = [y for x, y in frame_rounding_box]
        try:
            frame_roi = frame[min(y_pts):max(y_pts), min(x_pts):max(x_pts)]

            gray = cv2.cvtColor(frame_roi, cv2.COLOR_BGR2GRAY)

            ret, thresh = cv2.threshold(gray,0,255,cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU) 

            cv2.imshow("thresh", thresh)

            contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)                

            #temp, classified_points, means = cv2.kmeans(data=numpy.concatenate(contours), K=2, bestLabels=None,
            #                                            criteria=(cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_MAX_ITER, 1, 10), attempts=1, 
            #                                            flags=cv2.KMEANS_RANDOM_CENTERS)

            if True:
                cv2.drawContours(frame_roi, contours, -1,(0,255,0),3)

            all_contour = numpy.concatenate(contours)

            hull = cv2.convexHull(all_contour)
            if False:
                cv2.drawContours(frame_roi, hull, -1,(0,0,255), 4)

            if False:
                approx = cv2.approxPolyDP(numpy.concatenate(all_contour), 0.1*cv2.arcLength(all_contour, True),True)
                cv2.drawContours(frame_roi, approx, -1,(255,0,0), 4)

            for cnt in contours: 
                hull = cv2.convexHull(cnt)
                cv2.drawContours(frame_roi, hull, -1,(0,0,255), 2)

            cv2.imshow("ROI", frame_roi)
        except Exception,e: print str(e)

        #contours, hierarchy = cv2.findContours(, cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)

    
def main():
    cam = camera.CameraIterator(0)
    cam.start(processing, grayscale=True, size=(256, 256), workingCopy = True, show_frame=True, show_wframe=True)

if __name__ == '__main__':
    main()
