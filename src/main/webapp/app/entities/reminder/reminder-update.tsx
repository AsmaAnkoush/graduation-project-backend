import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAppointments } from 'app/entities/appointment/appointment.reducer';
import { getEntities as getParents } from 'app/entities/parent/parent.reducer';
import { createEntity, getEntity, reset, updateEntity } from './reminder.reducer';

export const ReminderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const appointments = useAppSelector(state => state.appointment.entities);
  const parents = useAppSelector(state => state.parent.entities);
  const reminderEntity = useAppSelector(state => state.reminder.entity);
  const loading = useAppSelector(state => state.reminder.loading);
  const updating = useAppSelector(state => state.reminder.updating);
  const updateSuccess = useAppSelector(state => state.reminder.updateSuccess);

  const handleClose = () => {
    navigate(`/reminder${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAppointments({}));
    dispatch(getParents({}));
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

    const entity = {
      ...reminderEntity,
      ...values,
      appointment: appointments.find(it => it.id.toString() === values.appointment?.toString()),
      recipient: parents.find(it => it.id.toString() === values.recipient?.toString()),
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
          ...reminderEntity,
          appointment: reminderEntity?.appointment?.id,
          recipient: reminderEntity?.recipient?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.reminder.home.createOrEditLabel" data-cy="ReminderCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.reminder.home.createOrEditLabel">Create or edit a Reminder</Translate>
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
                  id="reminder-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.reminder.messageText')}
                id="reminder-messageText"
                name="messageText"
                data-cy="messageText"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="reminder-appointment"
                name="appointment"
                data-cy="appointment"
                label={translate('smartVaxApp.reminder.appointment')}
                type="select"
              >
                <option value="" key="0" />
                {appointments
                  ? appointments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="reminder-recipient"
                name="recipient"
                data-cy="recipient"
                label={translate('smartVaxApp.reminder.recipient')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reminder" replace color="info">
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

export default ReminderUpdate;
