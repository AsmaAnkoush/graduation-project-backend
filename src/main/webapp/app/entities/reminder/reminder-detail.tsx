import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reminder.reducer';

export const ReminderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reminderEntity = useAppSelector(state => state.reminder.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reminderDetailsHeading">
          <Translate contentKey="smartVaxApp.reminder.detail.title">Reminder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reminderEntity.id}</dd>
          <dt>
            <span id="messageText">
              <Translate contentKey="smartVaxApp.reminder.messageText">Message Text</Translate>
            </span>
          </dt>
          <dd>{reminderEntity.messageText}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.reminder.appointment">Appointment</Translate>
          </dt>
          <dd>{reminderEntity.appointment ? reminderEntity.appointment.id : ''}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.reminder.recipient">Recipient</Translate>
          </dt>
          <dd>{reminderEntity.recipient ? reminderEntity.recipient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reminder" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reminder/${reminderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReminderDetail;
