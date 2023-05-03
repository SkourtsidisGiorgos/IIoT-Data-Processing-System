# syntax = docker/dockerfile:experimental
FROM ghcr.io/pymodbus-dev/pymodbus:dev

COPY ./generate_sensor_data.py .

RUN --mount=type=cache,mode=0755,target=/root/.cache/pip  pip install pandas numpy

CMD ["python", "generate_sensor_data.py"]
