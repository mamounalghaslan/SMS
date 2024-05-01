import sys
from ultralytics import YOLO
import json


def predict_image(_model_path, _image_path, _output_path):
    # Load the pre-trained YOLO model
    model = YOLO(_model_path)

    # Run inference
    results = model(_image_path)
    predictions = []

    for result in results:
        for box in result.boxes:
            predictions.append({
                'x1': int(box.xyxy[0][0]),
                'y1': int(box.xyxy[0][1]),
                'x2': int(box.xyxy[0][2]),
                'y2': int(box.xyxy[0][3]),
            })

    # Write the predictions to the output file
    with open(_output_path, 'w') as f:
        json.dump(predictions, f)

    print(f'Prediction written to {_output_path}')


if __name__ == '__main__':
    if len(sys.argv) != 4:
        print("Usage: python script.py model_path image_path output_path")
        sys.exit(1)

    _, model_path, image_path, output_path = sys.argv
    predict_image(model_path, image_path, output_path)
