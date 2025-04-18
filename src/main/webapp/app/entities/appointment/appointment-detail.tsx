import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './appointment.reducer';

export const AppointmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const appointmentEntity = useAppSelector(state => state.appointment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appointmentDetailsHeading">
          <Translate contentKey="smartVaxApp.appointment.detail.title">Appointment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.id}</dd>
          <dt>
            <span id="appointmentDate">
              <Translate contentKey="smartVaxApp.appointment.appointmentDate">Appointment Date</Translate>
            </span>
          </dt>
          <dd>
            {appointmentEntity.appointmentDate ? (
              <TextFormat value={appointmentEntity.appointmentDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="smartVaxApp.appointment.status">Status</Translate>
            </span>
          </dt>
          <dd>{appointmentEntity.status}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.appointment.parent">Parent</Translate>
          </dt>
          <dd>{appointmentEntity.parent ? appointmentEntity.parent.id : ''}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.appointment.child">Child</Translate>
          </dt>
          <dd>{appointmentEntity.child ? appointmentEntity.child.id : ''}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.appointment.schedule">Schedule</Translate>
          </dt>
          <dd>{appointmentEntity.schedule ? appointmentEntity.schedule.id : ''}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.appointment.healthWorker">Health Worker</Translate>
          </dt>
          <dd>{appointmentEntity.healthWorker ? appointmentEntity.healthWorker.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/appointment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/appointment/${appointmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppointmentDetail;
