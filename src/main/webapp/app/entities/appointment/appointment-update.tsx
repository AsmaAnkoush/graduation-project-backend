import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getParents } from 'app/entities/parent/parent.reducer';
import { getEntities as getChildren } from 'app/entities/child/child.reducer';
import { getEntities as getScheduleVaccinations } from 'app/entities/schedule-vaccination/schedule-vaccination.reducer';
import { getEntities as getHealthWorkers } from 'app/entities/health-worker/health-worker.reducer';
import { createEntity, getEntity, reset, updateEntity } from './appointment.reducer';

export const AppointmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const parents = useAppSelector(state => state.parent.entities);
  const children = useAppSelector(state => state.child.entities);
  const scheduleVaccinations = useAppSelector(state => state.scheduleVaccination.entities);
  const healthWorkers = useAppSelector(state => state.healthWorker.entities);
  const appointmentEntity = useAppSelector(state => state.appointment.entity);
  const loading = useAppSelector(state => state.appointment.loading);
  const updating = useAppSelector(state => state.appointment.updating);
  const updateSuccess = useAppSelector(state => state.appointment.updateSuccess);

  const handleClose = () => {
    navigate(`/appointment${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getParents({}));
    dispatch(getChildren({}));
    dispatch(getScheduleVaccinations({}));
    dispatch(getHealthWorkers({}));
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
    values.appointmentDate = convertDateTimeToServer(values.appointmentDate);

    const entity = {
      ...appointmentEntity,
      ...values,
      parent: parents.find(it => it.id.toString() === values.parent?.toString()),
      child: children.find(it => it.id.toString() === values.child?.toString()),
      schedule: scheduleVaccinations.find(it => it.id.toString() === values.schedule?.toString()),
      healthWorker: healthWorkers.find(it => it.id.toString() === values.healthWorker?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          appointmentDate: displayDefaultDateTime(),
        }
      : {
          ...appointmentEntity,
          appointmentDate: convertDateTimeFromServer(appointmentEntity.appointmentDate),
          parent: appointmentEntity?.parent?.id,
          child: appointmentEntity?.child?.id,
          schedule: appointmentEntity?.schedule?.id,
          healthWorker: appointmentEntity?.healthWorker?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.appointment.home.createOrEditLabel" data-cy="AppointmentCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.appointment.home.createOrEditLabel">Create or edit a Appointment</Translate>
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
                  id="appointment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.appointment.appointmentDate')}
                id="appointment-appointmentDate"
                name="appointmentDate"
                data-cy="appointmentDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.appointment.status')}
                id="appointment-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="appointment-parent"
                name="parent"
                data-cy="parent"
                label={translate('smartVaxApp.appointment.parent')}
                type="select"
              >
                <option value="" key="0" />
                {parents
                  ? parents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="appointment-child"
                name="child"
                data-cy="child"
                label={translate('smartVaxApp.appointment.child')}
                type="select"
              >
                <option value="" key="0" />
                {children
                  ? children.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="appointment-schedule"
                name="schedule"
                data-cy="schedule"
                label={translate('smartVaxApp.appointment.schedule')}
                type="select"
              >
                <option value="" key="0" />
                {scheduleVaccinations
                  ? scheduleVaccinations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="appointment-healthWorker"
                name="healthWorker"
                data-cy="healthWorker"
                label={translate('smartVaxApp.appointment.healthWorker')}
                type="select"
              >
                <option value="" key="0" />
                {healthWorkers
                  ? healthWorkers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/appointment" replace color="info">
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

export default AppointmentUpdate;
