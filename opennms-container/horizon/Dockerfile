ARG BASE_IMAGE="opennms/base-horizon"
ARG BASE_IMAGE_VERSION="jdk11-1.0.0"

FROM ${BASE_IMAGE}:${BASE_IMAGE_VERSION}

ARG VERSION=${BASE_IMAGE_VERSION}
ARG BUILD_DATE="1970-01-01T00:00:00+0000"

ARG PACKAGES="R-Core"
ARG CONFD_VERSION="0.16.0"
ARG CONFD_URL="https://github.com/kelseyhightower/confd/releases/download/v${CONFD_VERSION}/confd-${CONFD_VERSION}-linux-amd64"

ARG REPO_KEY_URL="https://yum.opennms.org/OPENNMS-GPG-KEY"
ARG REPO_RPM="https://yum.opennms.org/repofiles/opennms-repo-stable-rhel7.noarch.rpm"

ARG PACKAGES
ARG ONMS_PACKAGES="opennms-core opennms-webapp-jetty opennms-webapp-hawtio"

COPY ./rpms /tmp/rpms

SHELL ["/bin/bash", "-c"]

# Collect generic steps in a layer for caching
RUN setcap cap_net_raw+ep ${JAVA_HOME}/bin/java && \
    echo ${JAVA_HOME}/lib/jli > /etc/ld.so.conf.d/java-latest.conf && \
    ldconfig && \
    curl -L ${CONFD_URL} -o /usr/bin/confd && \
    chmod +x /usr/bin/confd && \
    yum -y install epel-release && \
    rpm --import "${REPO_KEY_URL}" && \
    yum -y install "${REPO_RPM}" && \
    yum -y install ${PACKAGES} && \
    yum clean all -y && \
    rm -rf /var/cache/yum

# Install repositories, system and OpenNMS packages and do some cleanup
RUN if [[ -n ${PACKAGES} ]]; then yum -y install ${PACKAGES}; fi && \
    if [ "$(ls -1 /tmp/rpms/*.rpm 2>/dev/null | wc -l)" != 0 ]; then yum -y localinstall /tmp/rpms/*.rpm; else yum -y install ${ONMS_PACKAGES}; fi && \
    rm -rf /tmp/rpms && \
    yum clean all && \
    rm -rf /var/cache/yum && \
    rm -rf /opt/opennms/logs \
           /var/opennms/rrd \
           /var/opennms/reports && \
    mkdir -p /opennms-data/logs \
             /opennms-data/rrd \
             /opennms-data/reports && \
    ln -s /opennms-data/logs /opt/opennms/logs && \
    ln -s /opennms-data/rrd /var/opennms/rrd && \
    ln -s /opennms-data/reports /var/opennms/reports

# Add templates replaced at runtime and entrypoint
COPY ./assets/*.tpl /root/
COPY ./assets/entrypoint.sh /

LABEL maintainer="The OpenNMS Group" \
      license="AGPLv3" \
      name="Horizon" \
      version="${VERSION}" \
      build.date="${BUILD_DATE}" \
      vendor="OpenNMS Community" \
      io.confd.version="${CONFD_VERSION}"

WORKDIR /opt/opennms

ENTRYPOINT [ "/entrypoint.sh" ]

STOPSIGNAL SIGTERM

CMD [ "-h" ]

### Runtime information and not relevant at build time

ENV OPENNMS_KARAF_SSH_HOST="0.0.0.0" \
    OPENNMS_KARAF_SSH_PORT="8101" \
    JAVA_OPTS=""

# Volumes for storing data outside of the container
VOLUME [ "/opt/opennms/etc", "/opt/opennms-etc-overlay", "/opennms-data" ]

##------------------------------------------------------------------------------
## EXPOSED PORTS
##------------------------------------------------------------------------------
## -- OpenNMS HTTP        8980/TCP
## -- OpenNMS JMX        18980/TCP
## -- OpenNMS KARAF RMI   1099/TCP
## -- OpenNMS KARAF SSH   8101/TCP
## -- OpenNMS MQ         61616/TCP
## -- OpenNMS Eventd      5817/TCP
## -- SNMP Trapd           162/UDP
## -- Syslog Receiver      514/UDP
EXPOSE 8980 8101 61616 162/udp 514/udp
