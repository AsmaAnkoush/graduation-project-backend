import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './parent.reducer';

export const ParentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const parentEntity = useAppSelector(state => state.parent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="parentDetailsHeading">
          <Translate contentKey="smartVaxApp.parent.detail.title">Parent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{parentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartVaxApp.parent.name">Name</Translate>
            </span>
          </dt>
          <dd>{parentEntity.name}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="smartVaxApp.parent.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{parentEntity.phone}</dd>
          <dt>
            <span id="dob">
              <Translate contentKey="smartVaxApp.parent.dob">Dob</Translate>
            </span>
          </dt>
          <dd>{parentEntity.dob ? <TextFormat value={parentEntity.dob} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="smartVaxApp.parent.role">Role</Translate>
            </span>
          </dt>
          <dd>{parentEntity.role}</dd>
        </dl>
        <Button tag={Link} to="/parent" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/parent/${parentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ParentDetail;
