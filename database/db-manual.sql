
create table CAMERA_STATUS_TYPES
(
    SYSTEM_ID   INTEGER auto_increment,
    DESCRIPTION CHARACTER VARYING not null,
    constraint CAMERA_STATUS_TYPES_PK
        primary key (SYSTEM_ID)
);

create table CAMERAS
(
    SYSTEM_ID      INTEGER auto_increment,
    IP_ADDRESS     CHARACTER VARYING not null,
    LOCATION       CHARACTER VARYING not null,
    USERNAME       CHARACTER VARYING,
    PASSWORD       CHARACTER VARYING,
    STATUS_TYPE_ID INTEGER           not null,
    constraint CAMERAS_PK
        primary key (SYSTEM_ID),
    constraint CAMERAS_STATUS_TYPE_FK
        foreign key (STATUS_TYPE_ID) references CAMERA_STATUS_TYPES
);

create table CAMERA_REFERENCE_IMAGES
(
    SYSTEM_ID    INTEGER auto_increment,
    CAMERA_ID    INTEGER           not null,
    IMAGE_PATH   CHARACTER VARYING not null,
    CAPTURE_DATE TIMESTAMP         not null,
    constraint CAMERA_REFERENCE_IMAGES_PK
        primary key (SYSTEM_ID),
    constraint CAMERA_REFERENCE_IMAGES_CAMERA_SYSTEM_ID_FK
        foreign key (CAMERA_ID) references CAMERAS
);

create table EMPLOYEE
(
    SYSTEM_ID INTEGER auto_increment,
    NAME      CHARACTER VARYING not null,
    MOBILE    CHARACTER VARYING not null,
    constraint EMPLOYEE_PK
        primary key (SYSTEM_ID)
);

create table MODEL_TYPES
(
    SYSTEM_ID             INTEGER auto_increment,
    NAME                  CHARACTER VARYING not null,
    PRETRAINED_MODEL_PATH CHARACTER VARYING not null,
    constraint MODEL_TYPES_PK
        primary key (SYSTEM_ID)
);

create table NOTIFICATIONS_ERROR_TYPES
(
    SYSTEM_ID   INTEGER auto_increment,
    DESCRIPTION CHARACTER VARYING not null,
    constraint ERROR_TYPES_PK
        primary key (SYSTEM_ID)
);

create table NOTIFICATION_STATUS_TYPES
(
    SYSTEM_ID   INTEGER auto_increment,
    DESCRIPTION CHARACTER VARYING not null,
    constraint NOTIFICATION_STATUS_TYPES_PK
        primary key (SYSTEM_ID)
);

create table PRODUCTS
(
    SYSTEM_ID INTEGER auto_increment,
    NAME      CHARACTER VARYING not null,
    LOCATION  CHARACTER VARYING not null,
    constraint PRODUCTS_PK
        primary key (SYSTEM_ID)
);

create table NOTIFICATIONS
(
    SYSTEM_ID                   INTEGER auto_increment,
    ERROR_TYPE_ID               INTEGER   not null,
    PRODUCT_ID                  INTEGER   not null,
    DETECTION_DATE              TIMESTAMP not null,
    NOTIFICATION_STATUS_TYPE_ID INTEGER   not null,
    constraint NOTIFICATIONS_PK
        primary key (SYSTEM_ID),
    constraint NOTIFICATIONS_ERROR_TYPE_FK
        foreign key (ERROR_TYPE_SYSTEM_ID) references NOTIFICATIONS_ERROR_TYPES,
    constraint NOTIFICATIONS_PRODUCT_SYSTEM_ID_FK
        foreign key (PRODUCT_SYSTEM_ID) references PRODUCTS,
    constraint NOTIFICATIONS_STATUS_TYPE_SYSTEM_ID_FK
        foreign key (NOTIFICATION_STATUS_TYPE_ID) references NOTIFICATION_STATUS_TYPES
);

create table PRODUCT_IMAGES
(
    PRODUCT_ID INTEGER           not null,
    IMAGE_PATH CHARACTER VARYING not null,
    SYSTEM_ID  INTEGER auto_increment,
    constraint PRODUCT_IMAGES_PK
        primary key (SYSTEM_ID),
    constraint PRODUCT_IMAGES_FK
        foreign key (PRODUCT_SYSTEM_ID) references PRODUCTS
);

create table PRODUCT_REFERENCES
(
    SYSTEM_ID  INTEGER auto_increment,
    CAMERA_ID  INTEGER          not null,
    PRODUCT_ID INTEGER          not null,
    X_CENTER   DOUBLE PRECISION not null,
    Y_CENTER   DOUBLE PRECISION not null,
    WIDTH      DOUBLE PRECISION not null,
    HEIGHT     DOUBLE PRECISION not null,
    constraint CAMERA_REFERENCES_PK
        primary key (SYSTEM_ID),
    constraint CAMERA_REFERENCES_CAMERA_ID
        foreign key (CAMERA_ID) references CAMERAS,
    constraint PRODUCT_REFERENCES_PRODUCT_SYSTEM_ID
        foreign key (PRODUCT_ID) references PRODUCTS
);

create table TRAINED_MODELS
(
    SYSTEM_ID     INTEGER auto_increment,
    MODEL_TYPE_ID INTEGER   not null,
    CREATION_DATE TIMESTAMP not null,
    constraint TRAINED_MODELS_PK
        primary key (SYSTEM_ID),
    constraint TRAINED_MODELS_FK
        foreign key (MODEL_TYPE_SYSTEM_ID) references MODEL_TYPES
);

create table MODEL_PRODUCTS_IMAGES
(
    SYSTEM_ID        INTEGER auto_increment,
    TRAINED_MODEL_ID INTEGER not null,
    PRODUCT_IMAGE_ID INTEGER not null,
    constraint MODEL_PRODUCTS_IMAGES_PK
        primary key (SYSTEM_ID),
    constraint MODEL_PRODUCTS_IMAGES_PRODUCT_IMAGE_FK
        foreign key (PRODUCT_IMAGE_SYSTEM_ID) references PRODUCT_IMAGES,
    constraint MODEL_PRODUCTS_IMAGES_TRAINED_MODEL_FK
        foreign key (TRAINED_MODEL_SYSTEM_ID) references TRAINED_MODELS
);

