(function () {
    "use strict";

    Ext.namespace('Hippo.Campus');

    Hippo.Campus.PardotCharacteristicPlugin =
        Ext.extend(Hippo.Targeting.CharacteristicPlugin, {
            constructor: function (config) {
                Hippo.Campus.PardotCharacteristicPlugin.superclass
                    .constructor.call(this, Ext.apply(config, {
                        visitorCharacteristic: {
                            targetingDataProperty: 'lists',
                            xtype: 'Hippo.Targeting.TargetingDataPropertyCharacteristic'
                        }
                    }));
            }

        });
}());
