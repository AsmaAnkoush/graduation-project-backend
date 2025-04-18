import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './schedule-vaccination.reducer';

export const ScheduleVaccinationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const scheduleVaccinationEntity = useAppSelector(state => state.scheduleVaccination.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="scheduleVaccinationDetailsHeading">
          <Translate contentKey="smartVaxApp.scheduleVaccination.detail.title">ScheduleVaccination</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{scheduleVaccinationEntity.id}</dd>
          <dt>
            <span id="scheduledDate">
              <Translate contentKey="smartVaxApp.scheduleVaccination.scheduledDate">Scheduled Date</Translate>
            </span>
          </dt>
          <dd>
            {scheduleVaccinationEntity.scheduledDate ? (
              <TextFormat value={scheduleVaccinationEntity.scheduledDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="smartVaxApp.scheduleVaccination.status">Status</Translate>
            </span>
          </dt>
          <dd>{scheduleVaccinationEntity.status}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.scheduleVaccination.child">Child</Translate>
          </dt>
          <dd>{scheduleVaccinationEntity.child ? scheduleVaccinationEntity.child.id : ''}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.scheduleVaccination.vaccination">Vaccination</Translate>
          </dt>
          <dd>{scheduleVaccinationEntity.vaccination ? scheduleVaccinationEntity.vaccination.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/schedule-vaccination" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/schedule-vaccination/${scheduleVaccinationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ScheduleVaccinationDetail;
