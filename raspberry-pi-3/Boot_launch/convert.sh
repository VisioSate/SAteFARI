#!/bin/bash

bash -c "source /home/visiosate/catkin_ws/devel/setup.bash && rosrun image_transport republish compressed in:=raspicam_node/image raw out:=raspicam_node/image_raw"
