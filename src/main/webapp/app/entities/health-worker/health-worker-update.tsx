import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './health-worker.reducer';

export const HealthWorkerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const healthWorkerEntity = useAppSelector(state => state.healthWorker.entity);
  const loading = useAppSelector(state => state.healthWorker.loading);
  const updating = useAppSelector(state => state.healthWorker.updating);
  const updateSuccess = useAppSelector(state => state.healthWorker.updateSuccess);

  const handleClose = () => {
    navigate(`/health-worker${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.age !== undefined && typeof values.age !== 'number') {
      values.age = Number(values.age);
    }

    const entity = {
      ...healthWorkerEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...healthWorkerEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.healthWorker.home.createOrEditLabel" data-cy="HealthWorkerCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.healthWorker.home.createOrEditLabel">Create or edit a HealthWorker</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="health-worker-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.username')}
                id="health-worker-username"
                name="username"
                data-cy="username"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.password')}
                id="health-worker-password"
                name="password"
                data-cy="password"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.phone')}
                id="health-worker-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.age')}
                id="health-worker-age"
                name="age"
                data-cy="age"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.name')}
                id="health-worker-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.gender')}
                id="health-worker-gender"
                name="gender"
                data-cy="gender"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.location')}
                id="health-worker-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.email')}
                id="health-worker-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.healthWorker.role')}
                id="health-worker-role"
                name="role"
                data-cy="role"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/health-worker" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default HealthWorkerUpdate;
