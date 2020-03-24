#!/usr/local/bin/python3
import json
from os import environ as env
from sys import argv
from pathlib import Path
import xml.etree.ElementTree as Xml
from xml.dom import minidom


def create_xml(file: Path, configuration: dict):
    root = Xml.Element('configuration')

    if configuration:
        for key, val in configuration.items():
            _property = Xml.Element('property')
            name = Xml.SubElement(_property, 'name')
            name.text = key
            value = Xml.SubElement(_property, 'value')
            value.text = str(val)
            root.append(_property)

    rough_string = Xml.tostring(root, 'utf-8')
    pretty_xml = minidom.parseString(rough_string).toprettyxml()

    with file.open(mode='wb') as xml_file:
        xml_file.write(bytes(pretty_xml, encoding='utf-8'))


def create_plain(file: Path, configuration: list):
    with file.open(mode='w') as plain_file:
        data = '\n'.join(configuration)
        plain_file.write(data)


def generate_configs(configuration: Path):
    with configuration.open(mode='r') as config_file:
        config = json.load(config_file)

    hbase_site = Path(env['HBASE_CONFIG']) / "hbase-site.xml"
    create_xml(file=hbase_site, configuration=config.get('hbase_site'))

    regionservers = Path(env['HBASE_CONFIG']) / "regionservers"
    create_plain(file=regionservers, configuration=config.get('regionservers'))

    backup_masters = Path(env['HBASE_CONFIG']) / "backup-masters"
    create_plain(file=backup_masters, configuration=config.get('backup_masters'))



generate_configs(configuration=Path(argv[1]))
